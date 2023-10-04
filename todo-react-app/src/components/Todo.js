import React, { Component, useState } from "react";
import {
  Checkbox,
  IconButton,
  InputBase,
  ListItem,
  ListItemSecondaryAction,
  ListItemText,
} from "@material-ui/core";
import DeleteOutlined from "@material-ui/icons/DeleteOutlined";

export default function Todo(props) {
  const [item, setItem] = useState(props.item);
  const [readOnly, setReadOnly] = useState(false);
  // 매개변수 item의 변수/값을 item에 대입
  const deleteItem = props.delete;
  const updateItem = props.update;

  const deleteEventHandler = () => {
    deleteItem(item);
  };
  const offReadOnlyMode = () => {
    console.log("Event!", readOnly);
    setReadOnly(false, () => {
      console.log("ReadOnly?", readOnly);
    });
  };
  const enterKeyEventHandler = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      setReadOnly(true);
      updateItem(item);
    }
  };
  const editEventHandler = (e) => {
    setItem((prevState)=>{
      let newState = {...prevState};
      newState.title = e.target.value;
      return newState;
    });
  };
  const checkboxEventHandler = (e) => {
    console.log("check box event call");
    // const thisItem = item;
    // thisItem.done = thisItem.done ? false : true; // thisItemdone = !thisitem.done
    setItem((prevState)=>{
        let newState = {...prevState};
        newState.done = prevState.done ? false : true;
        //console.log(newState.done);
        updateItem(newState);
        return newState;
    });
  };
  return (
    <ListItem>
      <Checkbox checked={item.done} onClick={checkboxEventHandler} />
      <ListItemText>
        <InputBase
          inputProps={{ "aria-label": "naked" }}
          type="text"
          id={item.id}
          name={item.id}
          value={item.title}
          multiline={true}
          fullWidth={true}
          onClick={offReadOnlyMode}
          onChange={editEventHandler}
          onKeyPress={enterKeyEventHandler}
        />
      </ListItemText>

      <ListItemSecondaryAction>
        <IconButton aria-label="Delete" onClick={deleteEventHandler}>
          <DeleteOutlined />
        </IconButton>
      </ListItemSecondaryAction>
    </ListItem>
  );
}
