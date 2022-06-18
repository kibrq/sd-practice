import React from "react";
import "../css/Background.scss";
import formStyles from "../css/Form.module.scss";
import styles from "../css/Upload.module.scss";
import Sidebar from "./Sidebar.js";
import {NavLink} from "react-router-dom";
import {STUDENT_SUBMISSIONS_URL} from "./Student";

const axios = require("axios").default;

export default class SubmissionUpload extends React.Component {
    defaultFormState = {
        taskId: "",
        repositoryUrl: ""
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
                    alert("Please enter submission " + field);
                    return;
                }
            }
        }

        axios.post(STUDENT_SUBMISSIONS_URL, {
            taskId: this.state.taskId,
            repositoryUrl: this.state.repositoryUrl
        }).then(() => {
            alert("Done");
            window.open("/student", "_self");
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
                        maxLength="10"
                        type="number"
                        min="1"
                        placeholder="Enter task id"
                        value={this.state.taskId}
                        onChange={this.handleChange("taskId")}
                    />
                    <input
                        className={formStyles.input}
                        maxLength="100"
                        type="text"
                        placeholder="Enter submission repository URL"
                        value={this.state.repositoryUrl}
                        onChange={this.handleChange("repositoryUrl")}
                    />
                    <input
                        className={formStyles.btn}
                        type="submit"
                        value="Upload"
                        onClick={this.upload}
                    />
                    <br/>
                    <NavLink className={styles.back} to="/student">
                        Back
                    </NavLink>
                </div>
            </div>
        );
    }
}
