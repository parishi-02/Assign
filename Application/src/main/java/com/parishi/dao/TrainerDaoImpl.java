package com.parishi.dao;

import com.parishi.dto.*;
import com.parishi.entity.*;
import com.parishi.utility.mapper.*;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class TrainerDaoImpl implements TrainerDao
{
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    Logger logger;
    @Override
    public List<TrainerDTO> getAllTrainers() {
        List<TrainerDTO> trainerDTOList = new ArrayList<>();
        try(Session session = sessionFactory.openSession()){
            List<Trainer> trainerList = session.createQuery("FROM Trainer", Trainer.class).getResultList();
            for(Trainer trainer : trainerList){
                TrainerDTO trainerDTO = TrainerMapper.fromEntityToDTO(trainer);
                trainerDTOList.add(trainerDTO);
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return trainerDTOList;
    }

    @Override
    public boolean registerTrainer(TrainerDTO trainerDto) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            trainerDto.setStatusFlag(0);
            Trainer trainer = TrainerMapper.fromDTOToEntity(trainerDto);
            session.saveOrUpdate(trainer);

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }




    @Override
    public boolean updateTrainer(TrainerDTO trainerDTO) {
        boolean flag = true;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Trainer trainer = TrainerMapper.fromDTOToEntity(trainerDTO);
            session.update(trainer);
            transaction.commit();
        }
        catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public TrainerDTO getTrainer(int trainerId) {
        TrainerDTO trainerDTO = null;
        try(Session session = sessionFactory.openSession()) {
            Trainer trainer = session.get(Trainer.class, trainerId);
            trainerDTO = TrainerMapper.fromEntityToDTO(trainer);

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();

        }
        return trainerDTO;
    }

    @Override
    public List<TrainerDTO> getAllTrainerWithStatusFlagZero()
    {
        List<TrainerDTO> trainerDTOList = new ArrayList<>();
        try(Session session = sessionFactory.openSession()){
            List<Trainer> trainerList = session.createNativeQuery(" select * from track_trainer where  status_flag= ?1", Trainer.class)
                    .setParameter(1,0)
                    .list();
            for(Trainer trainer : trainerList){
                TrainerDTO trainerDTO = TrainerMapper.fromEntityToDTO(trainer);
                trainerDTOList.add(trainerDTO);
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return trainerDTOList;
    }
}
