package com.my.task.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Task {

	private Integer taskNo;
	private String taskId;
	private Date dueDate1;
	private Date dueDate2;
	private String title;
	private String type;
	private Date regDate;
	private String endDate;
	private Double avgReviewScore;
	private List<Integer> answerList;
	
}
