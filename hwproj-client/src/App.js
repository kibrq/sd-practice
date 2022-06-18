import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Home from "./components/Home.js";
import NotFound from "./components/NotFound.js";
import Health from "./components/Health.js";
import Teacher from "./components/Teacher";
import Student from "./components/Student";
import TaskUpload from "./components/TaskUpload";
import CheckerUpload from "./components/CheckerUpload";
import SubmissionUpload from "./components/SubmissionUpload";

export default class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <div className="App">
                    <Routes>
                        <Route path="/" element={<Home/>} exact/>
                        <Route path="/health" element={<Health/>}/>
                        <Route path="/teacher" element={<Teacher/>}/>
                        <Route path="/teacher/task/upload" element={<TaskUpload/>}/>
                        <Route path="/teacher/checker/upload" element={<CheckerUpload/>}/>
                        <Route path="/student/submission/upload" element={<SubmissionUpload/>}/>
                        <Route path="/student" element={<Student/>}/>
                        <Route path="*" element={<NotFound/>}/>
                    </Routes>
                </div>
            </BrowserRouter>
        );
    }
}
