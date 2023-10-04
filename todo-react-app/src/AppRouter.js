import { Box } from "@material-ui/core";
import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "./App";
import Copyright from "./components/Copyright";
import Login from "./page/Login";
import Navigator from "./page/Navigator";
import Profile from "./page/Profile";
import SignUp from "./page/SignUp";

function AppRouter() {
  return (
    <BrowserRouter>
      <div>
        <Navigator />
      </div>
      <div>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/" element={<App />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
      <div>
        <Box mt={5}>
          <Copyright />
        </Box>
      </div>
    </BrowserRouter>
  );
}

export default AppRouter;
