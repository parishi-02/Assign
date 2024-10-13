package com.parishi.dao;

import com.parishi.dto.*;
import com.parishi.entity.*;
import com.parishi.utility.mapper.*;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class AdminDaoImpl implements AdminDao{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Logger logger;













}
