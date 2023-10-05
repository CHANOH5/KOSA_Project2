package com.my.task.service;

import com.my.exception.FindException;
import com.my.task.dto.TaskDTO;
import com.my.util.PageGroup;

public interface TaskService {

	/**
	 * 
	 * @param teamNo
	 * @param currentPage
	 * @return
	 * @throws FindException
	 */
	PageGroup<TaskDTO> findAllTaskList(Integer teamNo, int currentPage, boolean desc) throws FindException;
	
}
