package com.parishi.utility.mapper;

import com.parishi.dto.CoursePlanDTO;
import com.parishi.dto.DayWisePlanDTO;
import com.parishi.dto.HolidayDTO;
import com.parishi.entity.CoursePlan;
import com.parishi.entity.DayWisePlan;
import com.parishi.entity.Holiday;

import java.util.ArrayList;
import java.util.List;

public class HolidayMapper {

    public static HolidayDTO fromEntityToDTO(Holiday holiday) {
      HolidayDTO holidayDTO = new HolidayDTO();

      holidayDTO.setHolidayId(holiday.getHolidayId());
      holidayDTO.setHolidayType(holiday.getHolidayType());
      holidayDTO.setHolidayDate(holiday.getHolidayDate());
      holidayDTO.setHolidayName(holiday.getHolidayName());
      return holidayDTO;

    }



    //Method to convert DTO to Entity
    public static Holiday fromDTOToEntity(HolidayDTO holidayDTO) {
       Holiday holiday = new Holiday();
       holiday.setHolidayId(holidayDTO.getHolidayId());
       holiday.setHolidayType(holidayDTO.getHolidayType());
       holiday.setHolidayName(holidayDTO.getHolidayName());
       holiday.setHolidayDate(holidayDTO.getHolidayDate());

       return holiday;
    }
}
