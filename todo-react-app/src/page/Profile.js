import {
  Button,
  Container,
  Grid,
  TextField,
  Typography,
} from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import call from "../service/ApiService";

function Profile() {
  const [isLoading, setIsLoading] = useState(true);
  const [editMode, setEditMode] = useState(false);
  const [profile, setProfile] = useState();

  // 폼을 만들기 위한 여러가지 요소 불러오기
  const { register, handleSubmit, getValues } = useForm();

  // 데이터 전송시 작동할 함수 정의
  const onValid = (data) => {
    console.log(data);

    const { username, email } = getValues();
    setProfile((prevState) => {
      let newState = { ...prevState };
      newState.username = username;
      newState.email = email;
      //console.log(newState.done);
      updateInfo(newState);
      getInfo();
      return newState;
    });
  };

  const updateInfo = (item) => {
    console.log("profile update request", item);
    call("/auth/profile", "PUT", item)
      .then((response) => {
        console.log("profile update success");
        console.log("모드 변경");
        setEditMode(false);
        getInfo();
      })
  };

  const getInfo = () => {
    call("/auth/profile", "GET", null).then((response) => {
      setProfile(response); // update State
      setIsLoading(false);
      console.log(response);
      console.log("get profile from server");
    });
  };

  useEffect(() => {
    getInfo();
  }, []);

  return (
    <>
      {isLoading ? (
        // 로딩 중일 때
        <h1 style={{ textAlign: "center" }}>로딩중...</h1>
      ) : (
        <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
          <Grid container spacing={2}>
            <Typography component="h1" variant="h5">
              Profile
            </Typography>
          </Grid>
          <form noValidate onSubmit={handleSubmit(onValid)}>
            {" "}
            <Grid container spacing={2}>
              <Grid item xs={12}>
                {editMode ? (
                  <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="email"
                    label="이메일 주소"
                    name="email"
                    autoComplete="email"
                    defaultValue={profile.email}
                    {...register("email")}
                  />
                ) : (
                  <TextField
                    id="email"
                    label="email"
                    defaultValue={profile.email}
                    key={profile.email}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                )}
              </Grid>
              <Grid item xs={12}>
                {editMode ? (
                  <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="username"
                    label="이름"
                    name="username"
                    autoComplete="username"
                    defaultValue={profile.username}
                    {...register("username")}
                  />
                ) : (
                  <TextField
                    id="username"
                    label="username"
                    key={profile.username}
                    defaultValue={profile.username}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                )}
              </Grid>
              <Grid item xs={12}>
                {editMode ? (
                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                  >
                    수정완료
                  </Button>
                ) : (
                  <button
                    type="button"
                    onClick={() => {
                      setEditMode(true);
                    }}
                    fullWidth
                    variant="contained"
                    color="primary"
                  >
                    수정하기
                  </button>
                )}
              </Grid>
            </Grid>
          </form>
        </Container>
      )}
    </>
  );
}

export default Profile;
