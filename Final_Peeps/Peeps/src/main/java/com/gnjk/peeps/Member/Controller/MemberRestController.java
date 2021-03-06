package com.gnjk.peeps.Member.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnjk.peeps.Member.Service.UserDeleteService;
import com.gnjk.peeps.Member.Service.EditPwService;
import com.gnjk.peeps.Member.Service.FindPwService;
import com.gnjk.peeps.Member.Service.FindUserService;
import com.gnjk.peeps.Member.Service.FollowService;
import com.gnjk.peeps.Member.Service.LoginService;
import com.gnjk.peeps.Member.Service.MyPageService;
import com.gnjk.peeps.Member.Service.OAuthService;
import com.gnjk.peeps.Member.Service.RegService;
import com.gnjk.peeps.Member.Service.TimeLineService;
import com.gnjk.peeps.Member.domain.FollowRequest;
import com.gnjk.peeps.Member.domain.Peeps;
import com.gnjk.peeps.Member.domain.RegRequest;
import com.gnjk.peeps.Member.domain.SocialRequest;

@RestController
public class MemberRestController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private RegService regService;

	@Autowired
	private EditPwService editPwService;

	@Autowired
	private FindPwService findPwService;

	@Autowired
	private UserDeleteService deleteService;

	@Autowired
	private FindUserService findUserService;

	@Autowired
	private FollowService followService;

	@Autowired
	private MyPageService myPageService;

	@Autowired
	private OAuthService oauthService;

	@Autowired
	private TimeLineService timeLineService;

	// ?????????
	@PostMapping("/user/login")
	public int login(String email, String password, Model model, HttpServletRequest request, HttpSession session) {

		return loginService.login(email, password, request, session);

	}

	// ????????? ????????????
	@PostMapping("/member/regPost")
	public int memberReg(RegRequest regRequest, HttpServletRequest request) {

		return regService.memberReg(regRequest, request);
	}

	// ???????????? ??????
	@PostMapping("/user/editpw")
	public int EditPwPost(String email, String password, String e_password, String c_password) {

		return editPwService.EditPw(email, password, e_password, c_password);
	}

	// ???????????? ??????
	@PostMapping("/user/findPW")
	public int memberFindPost(String email, String id, HttpServletResponse response, @ModelAttribute Peeps peeps)
			throws Exception {

		return findPwService.find_pw(email, id, response, peeps);
	}

	// ????????????
	@PostMapping("/user/del")
	public int EditPwPost(HttpServletResponse response, String email, String password, int m_idx, String reason,
			HttpSession session) {

		session.invalidate();

		return deleteService.Delete(email, password, m_idx, reason);
	}

	// ??????
	@PostMapping("/user/loaduser")
	public List<Peeps> loadUser(@RequestParam("keyword") String keyword, @RequestParam("f_m_idx") int m_idx) {

		return findUserService.SearchPeeps(keyword, m_idx);
	}

	// ?????????
	@PostMapping("/follow")
	public int Follow(int m_idx, int y_idx) {

		return followService.follow(m_idx, y_idx);
	}

	// ????????????
	@PostMapping("/unfollow")
	public int UnFollow(int m_idx, int y_idx) {

		return followService.unfollow(m_idx, y_idx);
	}

	// ??????????????? ?????? ??????
	@PostMapping("/mypage/Info")
	public List<FollowRequest> PageInfo(@RequestParam("p_id") String id, @RequestParam("p_m_idx") int m_idx) {

		System.out.println(id);

		return myPageService.getPeeps(id, m_idx);
	}

	// ??????????????? ????????? ?????????
	@GetMapping("/mypage/ingList")
	public List<FollowRequest> ingList(int m_idx, int idx) {

		return myPageService.getFollowingList(m_idx, idx);
	}
	
	// ????????? ??????????????? ????????? ?????????
	@GetMapping("/mypage/UserList")
	public List<FollowRequest> UserList(int m_idx, int idx) {

		return myPageService.MyFollowingList(m_idx, idx);
	}

	// ??????????????? ????????? ?????????
	@GetMapping("/mypage/werList")
	public List<FollowRequest> werList(int m_idx, int idx) {

		return myPageService.getFollowerList(m_idx, idx);
	}

	// ??????????????? ?????????
	@PostMapping("/mypage/follow")
	public int MyFollow(int m_idx, int y_idx) {

		return myPageService.Follow(m_idx, y_idx);
	}

	// ??????????????? ????????????
	@PostMapping("/mypage/unfollow")
	public int MyUnFollow(int m_idx, int y_idx) {

		return myPageService.unFollow(m_idx, y_idx);
	}

	// ?????? ????????? ?????? ??????
	@GetMapping("/user/idCheck")
	public int emailCheck(@RequestParam("email") String email) {

		return oauthService.checkEmail(email);
	}

	// ?????? ?????? ?????? ??????
	@GetMapping("/user/photoChk")
	public String photoCheck(String email) {

		return oauthService.checkPhoto(email);
	}

	// ?????? ????????? ?????? ??????
	@RequestMapping(value = "/user/loginTypeChk", method = RequestMethod.GET)
	public String loginTypeCheck(@RequestParam("email") String email) {

		return oauthService.checkLoginType(email);
	}

	// ?????? ????????????
	@PostMapping(value = "/user/reg")
	public int memberReg(@ModelAttribute("socialData") SocialRequest socialRequest, HttpServletRequest request) {

		int result = oauthService.socialMemberReg(socialRequest, request);

		return result;
	}

	// ?????? ?????? ??????
	@PostMapping(value = "/user/photoUpdate")
	public int m_photoUpdate(String email, String m_photo, String name) {

		int result = oauthService.m_photoUpdate(email, m_photo, name);

		return result;
	}

	// ?????? ?????? ??????
	@GetMapping(value = "/user/socialInfo")
	public Peeps SocialInfo(String email, HttpServletRequest request, HttpSession session) {

		// session.setAttribute("peeps", oauthService.selectSocialInfo(email));

		return oauthService.selectSocialInfo(email, request, session);
	}

	// ?????? ????????? ?????? ??????
	@GetMapping("/user/socialVerify")
	public String socialVerify(String email) {

		return oauthService.selectSocialVerify(email);
	}

	@GetMapping("/user/followingList")
	// ?????? ????????? ?????????
	public List<Integer> followingList(int m_idx) {

		return timeLineService.FollowingList(m_idx);
	}

	// ????????? ?????? ?????? ????????????
	@GetMapping("/user/followingInfo")
	public List<FollowRequest> followingInfo(int m_idx) {

		return timeLineService.FollowingInfo(m_idx);
	}

	// ??????????????? - ????????? ??????
	@RequestMapping(value = "/mypage/chk", method = RequestMethod.GET)
	public String MyPageChk(int m_idx) {
		System.out.println("????????? ?????? ????????? : " + myPageService.selectId(m_idx));
		return myPageService.selectId(m_idx);
	}

	// 21.02.25 ?????? id ??? idx ?????? ?????? (??????)
	@GetMapping("/user/idxList")
	public List<Peeps> MemberidxList(@RequestParam Map<String, Object> param, HttpServletRequest request) {

		String memberid = request.getParameter("mId");
		System.out.println("path ??????????????? :" + memberid);

		return findUserService.getMemberidx(memberid);
	}

	// 21.02.25 ?????? idx ??? id ?????? ?????? (??????)
	@GetMapping("/user/idList")
	public List<Peeps> MemberidList(@RequestParam Map<String, Object> param, HttpServletRequest request) {

		int memberidx = Integer.parseInt(request.getParameter("mIdx"));

		return findUserService.getMemberid(memberidx);
	}

	// 21.02.26 ???????????? ?????? (??????)
	@GetMapping("/user/memberList")
	public List<Peeps> MemberList(HttpServletRequest request) {

		System.out.println("memberList ???????????? ??????~~~!");

		return findUserService.getMemberInfo();
	}

	// ????????? ??????
	@GetMapping("/user/loginChk")
	public boolean LoginChk(HttpServletRequest request, HttpServletResponse response) throws IOException {

		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		System.out.println(email);

		if (email == null) {
			return false;
		}

		return true;
	}

	// ?????? ?????? ??????
	@GetMapping("/user/random")
	public List<Peeps> RandomUser(int m_idx) {

		System.out.println("????????????");

		return timeLineService.randomUser(m_idx);
	}

	// 21.03.04 ?????????, ????????? ?????? ?????? (??????)
	@GetMapping("/user/chat")
	public List<Peeps> selectChat(int m_idx) throws Exception {

		System.out.println("chat ???????????? ????????????");

		return findUserService.getChat(m_idx);
	}
}
