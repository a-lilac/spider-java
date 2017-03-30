package com.carlos.repository;

import com.carlos.domain.Book;
//import com.carlos.domain.Book2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by Carlos on 2017/3/23.
 */
@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book,Integer> {
    //根据id查询
    Book findById(Integer id);
    
}
