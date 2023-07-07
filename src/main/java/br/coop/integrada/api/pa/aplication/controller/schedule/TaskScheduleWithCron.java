package br.coop.integrada.api.pa.aplication.controller.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/schedule")
@Tag(name = "Task Schedule", description = "Tarefas de integrações")
public class TaskScheduleWithCron {
	public String cron  = "0 */1 * * * *";

    @Autowired
    private TaskScheduler task;

    private ScheduledFuture<?> scheduledFuture;

    @GetMapping("/start")
    ResponseEntity<Void> start() {
        scheduledFuture = task.schedule(printHour(), new CronTrigger(cron));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/stop")
    ResponseEntity<Void> stop() {
        scheduledFuture.cancel(false);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private Runnable printHour() {
        return () -> System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    }
}