package com.parishi.dao;

import com.parishi.dto.BatchDTO;
import com.parishi.dto.MentoringSessionDto;
import com.parishi.entity.Batch;
import com.parishi.entity.MentoringSession;
import com.parishi.utility.mapper.BatchMapper;
import com.parishi.utility.mapper.MentoringSessionMapper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MentoringSessionDaoImpl implements MentoringSessionDao
{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Logger logger;
    @Override
    public MentoringSessionDto getMentoringSessionById(Integer sessionId) {
        MentoringSessionDto mentoringSessionDto = null;
        try(Session session = sessionFactory.openSession()) {
            MentoringSession mentoringSession = session.get(MentoringSession.class, sessionId);
            mentoringSessionDto  = MentoringSessionMapper.fromEntityToDTO(mentoringSession);

        }catch (Exception e){
            logger.error("Mentoring Session not found" + e.getMessage());
            e.printStackTrace();

        }
        return mentoringSessionDto;
    }

    @Override
    public boolean updateMentoringSession(MentoringSessionDto mentoringSessionDto) {
        try
        {
            Session session = sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();
            MentoringSession mentoringSession = MentoringSessionMapper.fromDTOToEntity(mentoringSessionDto);
            session.update(mentoringSession);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Mentoring Session not updated", e);
            return false;
        }
    }

    @Override
    public List<MentoringSessionDto> getAllMentoringSession() {
        List<MentoringSessionDto> mentoringSessionDtos = new ArrayList<>();
        try(Session session = sessionFactory.openSession()){
            List<MentoringSession> mentoringSessions = session.createQuery("FROM MentoringSession", MentoringSession.class).getResultList();
            for(MentoringSession mentoringSession : mentoringSessions){
                MentoringSessionDto mentoringSessionDto = MentoringSessionMapper.fromEntityToDTO(mentoringSession);
                mentoringSessionDtos.add(mentoringSessionDto);
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return mentoringSessionDtos;
    }

    @Override
    public boolean addMentoringSession(BatchDTO batchDTO) {
        try
        {
            Session session = sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();
            Batch batch = BatchMapper.fromDTOToEntity(batchDTO);
            session.update(batch);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Mentoring Session not added", e);
            return false;
        }
    }
}
