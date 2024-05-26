package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import io.lettuce.core.dynamic.annotation.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, Long>, JpaRepository<Meeting, Long>{

    //최신순
    //Page<Meeting> findPageAll(Pageable pageable);
    Page<Meeting> findPageByUserId(Long userId, Pageable pageable);
    //Optional<Meeting> findById(Long id);

//    @Query("SELECT m FROM Meeting m WHERE m.title LIKE %:keyword%")
//    Page<Meeting> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM meeting WHERE end_time IS NULL AND title LIKE %:keyword% ORDER BY ST_Distance_Sphere(location_point, :point)",
            countQuery = "SELECT count(*) FROM meeting WHERE end_time IS NULL AND title LIKE %:keyword%",
            nativeQuery = true)
    Page<Meeting> findPageByKeyword(@Param("keyword") String keyword, @Param("point") Point point, Pageable pageable);

    //Page<Meeting> findByCategoryId(Long categoryId, Pageable pageable);


//    @Query(value = "SELECT * FROM meeting WHERE ST_Distance_Sphere(location_point, :point) <= 1000",
//            nativeQuery = true)
//    Page<Meeting> findMeetingByDistance(@Param("point") Point point, Pageable pageable);

    @Query(value = "SELECT * FROM meeting WHERE end_time IS NULL ORDER BY ST_Distance_Sphere(location_point, :point)",
            countQuery = "SELECT count(*) FROM meeting WHERE end_time IS NULL",
            nativeQuery = true)
    Page<Meeting> findByLocationNear(@Param("point") Point point, Pageable pageable);

    @Query(value = "SELECT * FROM meeting WHERE Category_id = :category AND end_time IS NULL ORDER BY ST_Distance_Sphere(location_point, :point) ",
            countQuery = "SELECT count(*) FROM meeting WHERE Category_id = :category AND end_time IS NULL",
            nativeQuery = true)
    Page<Meeting> findByLocationNearAndCategory(@Param("point") Point point, @Param("category") Long category, Pageable pageable);
}
