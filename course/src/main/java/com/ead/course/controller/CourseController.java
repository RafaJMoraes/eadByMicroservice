package com.ead.course.controller;


import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Validated CourseDto courseDto){
       try{
            CourseModel course = new CourseModel();
            BeanUtils.copyProperties(courseDto, course);
            course.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            course.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(course));
        } catch (Exception exception){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
       }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(!courseModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted success!");
    }
    @PostMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@RequestBody  @Validated CourseDto courseDto,
                                               @PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        } else {
            CourseModel course = courseModelOptional.get();
            course.setName(courseDto.getName());
            course.setDescription(courseDto.getDescription());
            course.setImageUrl(courseDto.getImageUrl());
            course.setCourseStatus(courseDto.getCourseStatus());
            course.setCourseLevel(courseDto.getCourseLevel());

            course.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(course));
        }
    }

//    @GetMapping
//    public ResponseEntity<Object> getAllCourses(){
//        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
//    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId")UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCoursesPageable(SpecificationTemplate.UserSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "courseId",
                                                                direction = Sort.Direction.ASC) Pageable pageable){
        Page<CourseModel> courseModelPage = courseService.findAll(pageable, spec);
        return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
    }

}
