import React from "react";
import "../css/Background.scss";
import formStyles from "../css/Form.module.scss";
import styles from "../css/Upload.module.scss";
import Sidebar from "./Sidebar.js";
import {NavLink} from "react-router-dom";
import {TEACHER_TASKS_URL} from "./Teacher";

const axios = require("axios").default;
const UPLOAD = "/upload";
const TEACHER_UPLOAD_TASK = TEACHER_TASKS_URL + UPLOAD;

export default class TaskUpload extends React.Component {
    defaultFormState = {
        name: "",
        description: "",
        deadlineDate: "",
        checkerId: ""
    };

    constructor(props) {
        super(props);
        this.state = this.defaultFormState;
    }

    handleChange = id => (event) => {
        const next = event.target.value;
        let newState = {};
        newState[id] = next;
        this.setState(newState);
    };

    upload = () => {
        for (const field in this.state) {
            if (this.state.hasOwnProperty(field)) {
                if (this.state[field] === "") {
                    alert("Please enter task " + field);
                    return;
                }
            }
        }

        axios.post(TEACHER_UPLOAD_TASK, {
            name: this.state.name,
            description: this.state.description,
            deadlineDate: this.state.deadlineDate,
            checkerId: parseInt(this.state.checkerId)
        }).then(() => {
            alert("Done");
            window.open("/teacher", "_self");
            this.setState(this.defaultFormState);
        }).catch(error => {
            alert(error.response.status);
            console.log("Error occurred!");
            console.log(error);
        });
    };

    render() {
        return (
            <div>
                <Sidebar/>
                <div id="bg"/>
                <div className={formStyles.form}>
                    <input
                        className={formStyles.input}
                        maxLength="20"
                        type="text"
                        placeholder="Enter task name"
                        value={this.state.name}
                        onChange={this.handleChange("name")}
                    />
                    <textarea
                        className={formStyles.input}
                        rows="3"
                        placeholder="Enter task description"
                        value={this.state.description}
                        onChange={this.handleChange("description")}
                    />
                    <input
                        className={formStyles.input}
                        type="datetime-local"
                        value={this.state.deadlineDate}
                        onChange={this.handleChange("deadlineDate")}
                    />
                    <input
                        className={formStyles.input}
                        maxLength="10"
                        type="number"
                        min="1"
                        placeholder="Enter checker id"
                        value={this.state.checkerId}
                        onChange={this.handleChange("checkerId")}
                    />
                    <input
                        className={formStyles.btn}
                        type="submit"
                        value="Upload"
                        onClick={this.upload}
                    />
                    <br/>
                    <NavLink className={styles.back} to="/teacher">
                        Back
                    </NavLink>
                </div>
            </div>
        );
    }
}
