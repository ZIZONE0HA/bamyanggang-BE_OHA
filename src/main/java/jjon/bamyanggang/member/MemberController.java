package jjon.bamyanggang.member;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("")
public class MemberController {
	
	  private final MemberService memberService;
	  
	  @PostMapping("/addmember")
	  public ResponseEntity<String> addMember(@RequestBody MemberDto memberDto) {
	        // 이미지 파일이 전송되었는지 확인
		 

	        boolean success = memberService.addMember(memberDto);
	        if (success) {
	            return ResponseEntity.ok("회원가입이 성공적으로 처리되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("회원가입실패, ID ,닉네임, 이메일 , 휴대폰번호 중복검사 해주세요");
	        }
	    }
	  
	  @PostMapping("/addmember/image")
	  public String addMemberImage(@RequestPart("profileImage") MultipartFile profileImage)
	  {
	      
	          if (profileImage != null && !profileImage.isEmpty()) {
	              String imagePath = memberService.saveImage(profileImage);
	              if (imagePath != null) {
	                  return "프로필 이미지가 성공적으로 업로드되었습니다.";
	              } else {
	                  return "이미지 파일 저장에 실패했습니다.";
	              }
	          } else {
	              return "이미지 파일이 비어있습니다. 회원가입완료";
	          }
	     
	  }
	  
	  @PutMapping("/update/{userId}")
	  public ResponseEntity<String> updateMember(@PathVariable("userId") String userId, @RequestBody MemberDto memberDto) {
		  memberDto.setUserId(userId);
		  //memberDto.setUserId(memberDto.getUserId());
		 
		  
		  boolean success = memberService.updateMember(memberDto);
		 if(success) {
			 return ResponseEntity.ok("회원 수정 완료");
		 }
		 else {
			 return ResponseEntity.badRequest().body("회줭 수정 실패하였습니다.");
		 }
	  }
	  
	  
		  
		  @DeleteMapping("/deletemember")
		  public ResponseEntity<String> deleteDelete(@RequestBody MemberDto memberDto) {
			  
			  boolean succes = memberService.deleteMember(memberDto);
			  if(succes) {
				  return ResponseEntity.ok("회원 탈퇴원료");
			  }
			  	return ResponseEntity.badRequest().body("회원 탈퇴 실패");
			  
		  }
		  
			  @GetMapping("/userInfo/{userId}")
			    public MemberDto getUserByUserId(@PathVariable("userId") String userId) {
			      MemberDto memberDto =  memberService.getUserByUserId(userId);
			      
			      System.out.println(memberDto);
			      return  memberDto;
			      
			    }
	  
	// 중복 확인을 위한 엔드포인트 추가
      @GetMapping("/api/checkIdAvailability/idCheck/{userId}")
      public Integer checkIdAvailability(@PathVariable("userId") String userId) {
    	  int availability = memberService.isIdAvailable(userId);
    	    return availability;
    	    
      }

      @GetMapping("/checkIdAvailability/nickNameCheck")
      public Integer checkNickAvailability(@RequestParam String nickName) {
    	  int availability = memberService.isNickNameAvailable(nickName);
    	    return availability;
    	    
      }

      @GetMapping("/checkIdAvailability/emailCheck")
      public Integer checkEmailAvailability(@RequestParam String emailNum1) {
    	  int availability = memberService.isEmailAvailable(emailNum1);
    	    return availability;
    	    
      }
      @GetMapping("/checkIdAvailability/phoneNumCheck")
      public Integer checkPhoneAvailability(@RequestParam List<String> phoneNumbers) {
          String phoneNum1 = phoneNumbers.get(0);
          String phoneNum2 = phoneNumbers.get(1);
          String phoneNum3 = phoneNumbers.get(2);
          
          
          int availability = memberService.isPhoneNumAvailable(phoneNum1, phoneNum2, phoneNum3);
         
          return availability;
      }
}