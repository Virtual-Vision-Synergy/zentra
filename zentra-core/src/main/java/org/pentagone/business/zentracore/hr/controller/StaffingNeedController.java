package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.StaffingNeedDto;
import org.pentagone.business.zentracore.hr.service.StaffingNeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffing-needs")
public class StaffingNeedController {

    private final StaffingNeedService staffingNeedService;

    public StaffingNeedController(StaffingNeedService staffingNeedService) {
        this.staffingNeedService = staffingNeedService;
    }

    @PostMapping
    public ResponseEntity<StaffingNeedDto> createStaffingNeed(@RequestBody StaffingNeedDto staffingNeedDto) {
        StaffingNeedDto created = staffingNeedService.createStaffingNeed(staffingNeedDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffingNeedDto> updateStaffingNeed(
            @PathVariable Long id,
            @RequestBody StaffingNeedDto staffingNeedDto) {
        staffingNeedDto.setId(id);
        StaffingNeedDto updated = staffingNeedService.updateStaffingNeed(staffingNeedDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffingNeedDto> getStaffingNeedById(@PathVariable Long id) {
        StaffingNeedDto staffingNeed = staffingNeedService.getStaffingNeedById(id);
        return new ResponseEntity<>(staffingNeed, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StaffingNeedDto>> getAllStaffingNeeds() {
        List<StaffingNeedDto> staffingNeeds = staffingNeedService.getAllStaffingNeeds();
        return new ResponseEntity<>(staffingNeeds, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StaffingNeedDto>> getStaffingNeedsByDepartment(@PathVariable Long departmentId) {
        List<StaffingNeedDto> staffingNeeds = staffingNeedService.getStaffingNeedsByDepartment(departmentId);
        return new ResponseEntity<>(staffingNeeds, HttpStatus.OK);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<StaffingNeedDto>> getStaffingNeedsByJob(@PathVariable Long jobId) {
        List<StaffingNeedDto> staffingNeeds = staffingNeedService.getStaffingNeedsByJob(jobId);
        return new ResponseEntity<>(staffingNeeds, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<StaffingNeedDto>> getStaffingNeedsByStatus(@PathVariable String status) {
        List<StaffingNeedDto> staffingNeeds = staffingNeedService.getStaffingNeedsByStatus(status);
        return new ResponseEntity<>(staffingNeeds, HttpStatus.OK);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<StaffingNeedDto>> getStaffingNeedsByPriority(@PathVariable String priority) {
        List<StaffingNeedDto> staffingNeeds = staffingNeedService.getStaffingNeedsByPriority(priority);
        return new ResponseEntity<>(staffingNeeds, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaffingNeedById(@PathVariable Long id) {
        staffingNeedService.deleteStaffingNeedById(id);
        return ResponseEntity.noContent().build();
    }
}
