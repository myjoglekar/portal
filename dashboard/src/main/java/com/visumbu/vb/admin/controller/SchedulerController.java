/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.SchedulerService;
import com.visumbu.vb.admin.service.TimerService;
import com.visumbu.vb.bean.SchedulerBean;
import com.visumbu.vb.model.Scheduler;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author duc-dev-04
 */
@Controller
@RequestMapping("scheduler")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private TimerService timeService;

//    @RequestMapping(value = "scheduler", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    Scheduler createScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody Scheduler scheduler) {
//        return schedulerService.createScheduler(scheduler);
//    }
    
    @RequestMapping(value = "scheduler", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Scheduler createScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody SchedulerBean schedulerBean) {
        Scheduler scheduler = schedulerService.createScheduler(schedulerBean);
        if (scheduler.getSchedulerRepeatType().equalsIgnoreCase("Now")) {
            System.out.println("Test 1");
            List<Scheduler> scheduledTasks = new ArrayList<>();
            scheduledTasks.add(scheduler);
            timeService.executeTasks(scheduledTasks);
        }
        return scheduler;
    }

    @RequestMapping(value = "scheduler", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Scheduler updateScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody SchedulerBean schedulerBean) {
        Scheduler scheduler = schedulerService.updateScheduler(schedulerBean);
        if (scheduler.getSchedulerRepeatType().equalsIgnoreCase("Now")) {
            System.out.println("Test 1");
            List<Scheduler> scheduledTasks = new ArrayList<>();
            scheduledTasks.add(scheduler);
            timeService.executeTasks(scheduledTasks);
        }
        return scheduler;
    }

    @RequestMapping(value = "scheduler", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getScheduler(HttpServletRequest request, HttpServletResponse response) {
        return schedulerService.getScheduler();
    }

    @RequestMapping(value = "scheduler/{schedulerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Scheduler getSchedulerById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.getSchedulerById(schedulerId);
    }

    @RequestMapping(value = "schedulerHistory/{schedulerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getSchedulerHistoryById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.getSchedulerHistoryById(schedulerId);
    }
    
    @RequestMapping(value = "scheduler/{schedulerId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Scheduler deleteScheduler(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.deleteScheduler(schedulerId);
    }
    
    @RequestMapping(value = "schedulerStatus/enableOrDisable", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Scheduler updateSchedulerEnableDisable(HttpServletRequest request, HttpServletResponse response, @RequestBody Scheduler scheduler) {
        return schedulerService.updateSchedulerEnableDisable(scheduler);
    }
}
