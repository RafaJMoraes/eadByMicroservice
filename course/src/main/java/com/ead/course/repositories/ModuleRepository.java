package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    /** buscar atributo lazy em tempo de execução com EntityGraph  INIT **/
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);
    /**END**/


    /** query for consult native **/
    @Query(value = "select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId")UUID courseId);
    /** end */

    /**
     * @Modifying utilizado para update ou delete
     * */

}
