import { API_BASE_URL } from "../app-config";
const ACCESS_TOKEN = "ACCESS_TOKEN";

function call(api, method, request) {
  let headers = new Headers({
    "Content-Type": "application/json",
  });
  const accessToken = localStorage.getItem("ACCESS_TOKEN");

  if (accessToken) {
    headers.append("Authorization", "Bearer " + accessToken);
  }

  let options = {
    headers: headers,
    url: API_BASE_URL + api,
    method: method,
  };

  if (request) {
    options.body = JSON.stringify(request);
  }

  return fetch(options.url, options)
    .then((response) =>
      response.json().then((json) => {
        if (!response.ok) {
          return Promise.reject(json);
        }
        return json;
      })
    )
    .catch((error) => {
      console.log(error.status);
      if (error.status === 403) {
        window.location.href = "/login";
      }
      return Promise.reject(error);
    });
}

// 로그인을 위한 API 서비스 메소드 signin
export function signin(userDto) {
    return call("/auth/signin","POST",userDto)
    .then((response) => {
        if(response.token) {
            // local 스토리지에 토큰 저장
            localStorage.setItem("ACCESS_TOKEN", response.token);
            // token이 존재하는 경우 todo 화면으로 리다이렉트
            window.location.href="/";
        }
    })
}

// 회원가입 요청
export function signup(userDto) {
    return call("/auth/signup","POST",userDto)
    .then((response) => {
        if(response.id) {
            window.location.href="/";
        }
    })
    .catch((error) => {
        console.log(error.status);
        if(error.status === 403){
            window.location.href = "/auth/signup"
        }
        return Promise.reject(error);
    })
}

// 로그아웃
export function signout() {
    // local 스토리지에 토큰 삭제
    localStorage.setItem("ACCESS_TOKEN", null);
    window.location.href = "/";
}

// 인증코드 메일로 보내기
export function sendEmail(email) {
  console.log(`/mail/send?email=${email}`);
  return call(`/mail/send?email=${email}`,"POST",null)
    .then((response) => {
        alert("메일로 전송했습니다. 메일함을 확인해주세요.")
    })
    .catch((error)=>{
      alert("메일 보내기에 실패했습니다.");
      console.log(error);
    })
}

// 인증코드 검증하기
export function checkCertificateCode(code) {
  console.log(`/mail/check?code=${code}`);
  return call(`/mail/check?code=${code}`, "GET", null)
  .then((response) => {
      console.log(response,"인증코드 확인 성공");
  })
  .catch((error) => {
      console.log(error.status);
      if(error.status === 400){
          alert("인증코드 검증이 실패했습니다.");
      }
  });
}

export default call;
