import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Home from "./components/Home.js";
import NotFound from "./components/NotFound.js";
import Health from "./components/Health.js";

export default class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <div className="App">
                    <Routes>
                        <Route path="/" element={<Home/>} exact/>
                        <Route path="/health" element={<Health/>}/>
                        <Route path="*" element={<NotFound/>}/>
                    </Routes>
                </div>
            </BrowserRouter>
        );
    }
}
