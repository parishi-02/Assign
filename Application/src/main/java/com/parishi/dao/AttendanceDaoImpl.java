package com.parishi.dao;

import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.AttendanceDTO;
import com.parishi.dto.BatchDTO;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Attendance;
import com.parishi.entity.Batch;
import com.parishi.entity.Trainee;
import com.parishi.utility.mapper.AssignmentPlanMapper;
import com.parishi.utility.mapper.AttendanceMapper;
import com.parishi.utility.mapper.BatchMapper;
import com.parishi.utility.mapper.TraineeMapper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AttendanceDaoImpl implements AttendanceDao
{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Logger logger;

    @Override
    public boolean saveAttendance(BatchDTO batchDTO) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Batch batch = BatchMapper.fromDTOToEntity(batchDTO);
            System.out.println("batch attendnace in controller = " +batch);
            session.update(batch);
            transaction.commit();
            return true;
        }
        catch (HibernateException e) {
            logger.error("Attendance not updated" + e.getMessage());
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public AttendanceDTO getAttendanceDataByDate(LocalDate attendanceDate) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Attendance a WHERE a.attendanceDate = :attendanceDate";

            // Create Query
            Query query = session.createQuery(hql);
            query.setParameter("attendanceDate", attendanceDate);

            // Execute Query
            List<Attendance> results = query.list();

            if(results!=null && !results.isEmpty())
            {
                Attendance attendance = results.get(0);
                AttendanceDTO attendanceDTO = AttendanceMapper.fromEntityToDTO(attendance);
                return attendanceDTO;
            }
            return null;
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AttendanceDTO getAttendanceDtoById(Integer attendanceId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Attendance a WHERE a.attendanceId = :attendanceId";

            // Create Query
            Query query = session.createQuery(hql);
            query.setParameter("attendanceId", attendanceId);

            // Execute Query
            List<Attendance> results = query.list();

            System.out.println("result attendnace= " + results);

            if (results != null && !results.isEmpty()) {
                Attendance attendance = results.get(0);
                AttendanceDTO attendanceDTO = AttendanceMapper.fromEntityToDTO(attendance);
                System.out.println("attendnace = " + attendanceDTO);
                return attendanceDTO;
            }
            return null;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateAttendance(AttendanceDTO attendanceDTO) {
        try( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Attendance attendance = AttendanceMapper.fromDTOToEntity(attendanceDTO);
            System.out.println("attendance in dao = " + attendance);
            session.update(attendance);
            transaction.commit();

        }catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
