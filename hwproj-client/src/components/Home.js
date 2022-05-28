import React from "react";
import {NavLink} from "react-router-dom";
import "../css/Background.scss";
import styles from "../css/Home.module.scss";
import Sidebar from "./Sidebar.js";

export default class Home extends React.Component {
    render() {
        return (
            <div className="Home">
                <Sidebar/>
                <div id="bg"/>
                <h1 className={styles.header}>Hwproj</h1>
                <NavLink className={styles.link} to="/teacher">
                    <button className={styles.loginBtn}>
                        Teacher
                    </button>
                </NavLink>
                <NavLink className={styles.link} to="/student">
                    <button className={styles.loginBtn}>
                        Student
                    </button>
                </NavLink>
            </div>
        );
    }
}
