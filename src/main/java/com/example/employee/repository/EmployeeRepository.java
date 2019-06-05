package com.example.employee.repository;

import com.example.employee.entity.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    //以下所有的*都代表变量

    //1.查询名字是*的第一个employee
    public Employee findByName(String name);
    //2.找出Employee表中第一个姓名包含`*`字符并且薪资大于*的雇员个人信息
    @Query("from Employee where name like %?1% and salary > ?2")
    public List<Employee> findtest(String name, Integer salary);
    //3.找出一个薪资最高且公司ID是*的雇员以及该雇员的姓名
    @Query("from Employee where companyId = :companyId and salary = (select max(salary) from Employee where companyId = :companyId)")
    public Employee findMaxSalaryByCompanyId(@Param("companyId")int companyId);

    //4.实现对Employee的分页查询，每页两个数据
    Page<Employee> findAll(Pageable pageable);
    //5.查找**的所在的公司的公司名称
    @Query(nativeQuery=true, value = "select companyName from employee e inner join company c on e.companyId = c.id where e.name = ?1")
    public String findCompanyByEmployeeName(String name);
    //6.将*的名字改成*,输出这次修改影响的行数

    @Modifying(clearAutomatically = true)
    @Query(value = "update employee e set e.name =?2 where e.name = ?1",nativeQuery = true)
    int updateName(String oldName, String newName);
    //7.删除姓名是*的employee
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from employee e where e.name = ?1",nativeQuery = true)
    void deleteByName(String name);
}
