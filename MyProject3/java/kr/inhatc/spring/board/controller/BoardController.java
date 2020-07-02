package kr.inhatc.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.FileDto;
import kr.inhatc.spring.board.service.BoardService;

@Controller
public class BoardController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Autowired //자동으로 서비스를 불러온다.
	private BoardService boardService;
	
//	@RequestMapping("/")
//	public String hello() {
//		return "index";
//	}
	
	@RequestMapping("/test/testPage")// 테스트용입니다.
	public String testPage() { 
		return "test/testPage"; 
	}
	
	//리스트를 보러 이동을 하려한다.
	@RequestMapping("/board/boardList")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardList(Model model) { //ModelAndView에서 바꾼거임
		//ModelAndView mv = new ModelAndView(); //ModelAndView 나중에 사용할 때 참고
		//mv.addObject("list", list);
		
		List<BoardDto> list = boardService.boardList();
		log.debug("============>" + list.size());//boardList("board/boardList")
		System.out.println("============>" + list.size());
		model.addAttribute("list", list);
		
		return "board/boardList"; //mv
	}
	
	@RequestMapping("/board/boardWrite")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardWrite() { 
		return "board/boardWrite"; 
	}
	
	@RequestMapping("/board/boardInsert")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardInsert(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) {
		boardService.boardInsert(board, multipartHttpServletRequest);
		return "redirect:/board/boardList"; 
	}
	
	@RequestMapping("/board/boardDetail")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardDetail(@RequestParam int boardIdx, Model model) {
		BoardDto board = boardService.boardDetail(boardIdx);
		model.addAttribute("board", board);
		//System.out.println(board);
		return "board/boardDetail"; 
	}
	
	@RequestMapping("/board/boardUpdate")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardUpdate(BoardDto board) {
		boardService.boardUpdate(board);
		//boardService.boardInsert(board);
		return "redirect:/board/boardList"; 
	}
	
	@GetMapping @PostMapping @DeleteMapping @PutMapping
	@RequestMapping("/board/boardDelete")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public String boardDelete(@RequestParam("boardIdx") int boardIdx) {
		boardService.boardDelete(boardIdx);
		//boardService.boardInsert(board);
		return "redirect:/board/boardList"; 
	}
	
	@RequestMapping("/board/downloadBoardFile")//이동하려는 주소//이동하게 될 웹사이트의 주소나 거의 같게 적어주는 것이 일반적입니다.
	public void downloadBoardFile(
			@RequestParam("idx") int idx,
			@RequestParam("boardIdx") int boardIdx,
			HttpServletResponse response) throws IOException {
		
		FileDto boardFile = boardService.selectFileInfo(idx, boardIdx);
		
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();
			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
			
			// response 헤더에 설정
			response.setContentType("application/octet-stream");
			response.setContentLength(files.length);
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			
			response.getOutputStream().write(files);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
}
