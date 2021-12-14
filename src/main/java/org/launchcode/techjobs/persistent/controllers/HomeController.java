package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    public EmployerRepository employerRepository;
    @Autowired
    public SkillRepository skillRepository;
    @Autowired
    public JobRepository jobRepository;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "My Jobs");
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());

        // Get all skills from the skillsRepository and put them in a List of SKill objects.  Max says cast...
        List<Skill> skills = (List <Skill>) skillRepository.findAll();
        model.addAttribute("skills", skills);

        // Get all employers from the employerRepository and put them in a List of Employer objects
        List<Employer> employers = (List <Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);

//         model.addAttribute("skills", (List <Skill>)skillRepository.findAll()); //cast to List of Skill?



        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        // get the employer from the employerRepository that has the employerId selected from the drop menu
        //Employer employer = employerRepository.findById(employerId);
        Employer employer = employerRepository.findById(employerId).orElse(new Employer());
        // get the skills form the skillRepository that have the skillId's selected from the checkboxes
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);

        // add the employer to the newJob
        newJob.setEmployer(employer);

        // save the new Job to the jobRepository
        jobRepository.save(newJob);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        Optional optJob = employerRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "job/view";
        } else {

            return "view";
        }
    }
}
