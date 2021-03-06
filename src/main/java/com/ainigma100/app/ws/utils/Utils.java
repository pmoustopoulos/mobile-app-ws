package com.ainigma100.app.ws.utils;

import com.ainigma100.app.ws.utils.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Utils {

    private final JWTTokenProvider jwtTokenProvider;


    public String generatePasswordResetToken(String id) {
        return jwtTokenProvider.generateJwtToken(id);
    }

    public String generateEmailVerificationToken(String id) {
        return jwtTokenProvider.generateJwtToken(id);
    }

    public boolean isTokenExpired(String token) {
        return jwtTokenProvider.isTokenExpired(token);
    }

    public String generateRandomId() {
        return UUID.randomUUID().toString();
    }


    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {

        ModelMapper modelMapper = new ModelMapper();

        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }


    public <T> T map(Object source, Class<T> targetClass) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(source, targetClass);
    }


    public <S, T> Page<T> mapPage(Page<S> source, Class<T> targetClass) {

        ModelMapper modelMapper = new ModelMapper();

        List<S> pageAsList = source.getContent();

        List<T> mappedList = pageAsList
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());

        return new PageImpl<>(mappedList, PageRequest.of(source.getNumber(), source.getSize(), source.getSort()),
                source.getTotalElements());
    }



    /**
     * This method is used to create a pagination based on specific attributes.
     *
     * @param sortList must not be {@literal null}. It represents the list of attributes that will be used to 'Order By' the results
     * @param page is of type Integer which represents the page number
     * @param size is of type Integer which represents the size of the results
     * @return Pageable
     */
    public Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

        List<Sort.Order> orders = new ArrayList<>();

        if(sortList != null) {
            // iterate the SortList to see based on which attributes we are going to Order By the results.
            for(SortItem sortValue : sortList) {
                orders.add(new Order(sortValue.getDirection(), sortValue.getField()));
            }
        }

        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 0;
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }


    /**
     * This method is used to set the value of the cell along
     * with the cell style.
     *
     * @param inputValue of type Date
     * @param row
     * @param columnIndex
     * @param formatDateCellStyle
     */
    public static void setCellValue(Date inputValue, Row row, int columnIndex, CellStyle formatDateCellStyle) {

        Cell cellValue = row.createCell(columnIndex);
        if (inputValue == null) {
            cellValue.setCellValue("");
        } else {
            cellValue.setCellValue(inputValue);
            cellValue.setCellStyle(formatDateCellStyle);
        }
    }


    /**
     * This method is used to set the value of the cell along
     * with the cell style.
     *
     * @param inputValue of type LocalDate
     * @param row
     * @param columnIndex
     * @param formatDateCellStyle
     */
    public static void setCellValue(LocalDate inputValue, Row row, int columnIndex, CellStyle formatDateCellStyle) {

        Cell cellValue = row.createCell(columnIndex);
        if (inputValue == null) {
            cellValue.setCellValue("");
        } else {
            cellValue.setCellValue(inputValue);
            cellValue.setCellStyle(formatDateCellStyle);
        }
    }

    /**
     * This method is used to set the value of the cell along
     * with the cell style.
     *
     * @param inputValue of type LocalDate
     * @param row
     * @param columnIndex
     * @param formatDateCellStyle
     */
    public static void setCellValue(LocalDateTime inputValue, Row row, int columnIndex, CellStyle formatDateCellStyle) {

        Cell cellValue = row.createCell(columnIndex);
        if (inputValue == null) {
            cellValue.setCellValue("");
        } else {
            cellValue.setCellValue(inputValue);
            cellValue.setCellStyle(formatDateCellStyle);
        }
    }


    /**
     * This method is used to set the value of the cell along
     * with the cell style.
     *
     * @param inputValue of type String
     * @param row
     * @param columnIndex
     * @param formatTextCellStyle
     */
    public static void setCellValue(String inputValue, Row row, int columnIndex, CellStyle formatTextCellStyle) {

        Cell cellValue = row.createCell(columnIndex);
        if (inputValue == null) {
            cellValue.setCellValue("");
        } else {
            cellValue.setCellValue(inputValue);
            cellValue.setCellStyle(formatTextCellStyle);
        }
    }

    /**
     * This method is used to set the value of the cell along
     * with the cell style.
     *
     * @param inputValue of type BigDecimal
     * @param row
     * @param columnIndex
     * @param formatNumberCellStyle
     */
    public static void setCellValue(BigDecimal inputValue, Row row, int columnIndex, CellStyle formatNumberCellStyle) {

        Cell cellValue = row.createCell(columnIndex);
        if (inputValue == null) {
            cellValue.setCellValue("");
        } else {
            cellValue.setCellValue(String.valueOf(inputValue));
            cellValue.setCellStyle(formatNumberCellStyle);
        }
    }

}
