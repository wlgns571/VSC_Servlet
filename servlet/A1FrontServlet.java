package com.study.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public class A1FrontServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// freeList로 왔을 땐 freeList 코드 실행 후 forward
		// freeView로 왔을 땐 freeView 코드 실행 후 forward
		// freeEdit로 왔을 땐 freeEdit 코드 실행 후 forward
		String uri = req.getRequestURI();
		uri = uri.substring(req.getContextPath().length());
		System.out.println(uri);
		String viewPage = "";
		if (uri.equals("/free/freeList.wow")) {
			FreeBoardSearchVO searchVO = new FreeBoardSearchVO();
			try {
				BeanUtils.populate(searchVO, req.getParameterMap());
			} catch (IllegalAccessException | InvocationTargetException e) {
				System.out.println("파라미터가 세팅이 되지 않음");
				e.printStackTrace();
			}
			req.setAttribute("searchVO", searchVO);
			IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
			List<FreeBoardVO> freList = freeBoardService.getBoardList(searchVO);
			req.setAttribute("freList", freList);
			ICommCodeService codeService = new CommCodeServiceImpl();
			List<CodeVO> cateList = codeService.getCodeListByParent("BC00");
			req.setAttribute("cateList", cateList);
			viewPage = "/WEB-INF/views/free/freeList.jsp";
			
		} else if (uri.equals("/free/freeView.wow")) {
			
			int boNo = Integer.parseInt(req.getParameter("boNo"));
			IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
			try {
				FreeBoardVO free = freeBoardService.getBoard(boNo);
				freeBoardService.increaseHit(boNo);
				req.setAttribute("free", free);
			} catch (BizNotFoundException | BizNotEffectedException bnf) {
				req.setAttribute("bnf", bnf);
			}
			viewPage = "/WEB-INF/views/free/freeView.jsp";
			
		} else if (uri.equals("/free/freeEdit.wow")) {
			
		}
		RequestDispatcher rd = req.getRequestDispatcher(viewPage);
		rd.forward(req, resp);
	}
}
