import {
  Button,
  Container,
  FormControl,
  Grid,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  TextField,
  Typography,
} from "@material-ui/core";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import React, { Component, useState } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import { checkCertificateCode, sendEmail, signup } from "../service/ApiService";
import "./SignUp.css"

export default function SignUp() {
  const [isChecked, setIsChecked] = useState(false);

  // 폼을 만들기 위한 여러가지 요소 불러오기
  const { register, handleSubmit, getValues } = useForm();
  
  // 비밀번호 박스 암호화
  const [showPassword, setShowPassword] = React.useState(false);

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };

  // 데이터 전송시 작동할 함수 정의
  const onValid = (data) => {
    console.log(data);

    const { username, email, password } = getValues();
    signup({ email: email, username: username, password: password }).then(
      (respone) => {
        window.location.href = "/login";
      }
    );
  };

  return (
    <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
      <form noValidate onSubmit={handleSubmit(onValid)}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography component="h1" variant="h5">
              계정 생성
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="username"
              name="username"
              variant="outlined"
              required
              fullWidth
              id="username"
              label="사용자 이름"
              autoFocus
              {...register("username")}
            />
          </Grid>
          <Grid container spacing={12}>
            <Grid item xs={10}>
            <TextField
              autoComplete="email"
              name="email"
              variant="outlined"
              required
              fullWidth
              id="email"
              label="이메일 주소"
              autoFocus
              {...register("email")}
            />
            </Grid>
            <Grid item xs={2}>
            <div className="user-btn" type="button" onClick={()=>{const {email} = getValues(); sendEmail(email); } }>
              코드전송
            </div>
            </Grid>
          </Grid>
          <Grid container spacing={12}>
            <Grid item xs={10}>
            <TextField
              required
              fullWidth
              id="email"
              label="인증 코드"
              autoFocus
              {...register("code")}
            />
            </Grid>
            <Grid item xs={2}>
            <div className="user-btn" type="button" onClick={()=>{const {code} = getValues(); checkCertificateCode(code).then(()=>setIsChecked(true));}} fullWidth>
              확인
            </div>
            </Grid>
          </Grid>
          <Grid item xs={12}>
            <FormControl {...register("password")} fullWidth sx={{ m: 1 }} variant="outlined">
              <InputLabel required >패스워드</InputLabel>
              <OutlinedInput
                type={showPassword ? "text" : "password"}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
              />
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <Button disabled={isChecked? false : true} type="submit" fullWidth variant="contained" color="primary">
              계정생성
            </Button>
          </Grid>
        </Grid>
        <Grid container justifyContent="flex-end">
          <Grid item>
            <Link to="/login" variant="body2">
              이미 계정이 있습니까? 로그인 하세요.
            </Link>
          </Grid>
        </Grid>
      </form>
    </Container>
  );
}
