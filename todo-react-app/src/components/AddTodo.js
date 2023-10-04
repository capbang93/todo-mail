import { Button, Grid, Paper, TextField } from "@material-ui/core";
import React, { Component, useState } from "react";

export default function AddTodo(props) {
    const [title,setTitle] = useState("");
    const [item, setItem] = useState();
    const add = props.add;
    // props의 함수를 this.add에 연결, props에는 상위 컴포넌트(App.js)의 함수, 매개변수가 들어있다.
    const completeItem = props.deleteComplete;

  const onInputChange = (e) => {
    console.log(e.target.value);
    setTitle(e.target.value);
    setItem({title:e.target.value});
  };
  const onButtonClick = () => {
    add(item);
    setTitle("");
    // text 값을 추가하고 입력 필드는 초기화시킨다
  };

  const enterKeyEventHandler = (e) => {
    if (e.key == "Enter") {
      onButtonClick();
    }
  };

    return (
      <Paper style={{ margin: 16, padding: 16 }}>
        <Grid container>
          <Grid xs={11} md={11} item style={{ paddingRight: 16 }}>
            <TextField
              placeholder="Add Todo here"
              fullWidth
              onChange={onInputChange}
              value={title}
              onKeyPress={enterKeyEventHandler}
            />
          </Grid>
          <Grid xs={1} md={1} item>
            <Button fullWidth color="secondary" variant="outlined" onClick={onButtonClick}>
                +
            </Button>
          </Grid>
        </Grid>
      </Paper>
    );
  }

