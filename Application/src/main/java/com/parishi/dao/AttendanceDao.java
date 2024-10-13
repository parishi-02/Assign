package com.parishi.dao;

import com.parishi.dto.AttendanceDTO;
import com.parishi.dto.BatchDTO;

import java.time.LocalDate;

public interface AttendanceDao
{
    boolean saveAttendance(BatchDTO batchDTO);
    AttendanceDTO getAttendanceDataByDate(LocalDate attendanceDate);
    AttendanceDTO getAttendanceDtoById(Integer attendanceId);
    boolean updateAttendance(AttendanceDTO attendanceDTO);

}
