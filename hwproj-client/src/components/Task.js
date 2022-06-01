import React from "react";
import {TEACHER_TASKS_URL} from "./Teacher.js";
import styles from "../css/Task.module.scss";

const axios = require("axios").default;

export default class Task extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            task: null
        };
    }

    getTask = async id => {
        let task = null;
        let url = TEACHER_TASKS_URL + "?id=" + id;
        await axios.get(url).then(result => {
            task = result.data;
        });
        return task;
    };

    componentDidMount() {
        this.getTask(this.props.task.id).then(task => {
            this.setState({
                task: task
            });
        }).catch(error => {
            console.log("Error occurred!");
            console.log(error);
        });
    }

    render() {
        if (this.props.task == null || this.state.task == null) return <span>Loading task...</span>;

        return (
            <div>
                ID: {this.state.task.id}
                <br/>
                <div className={styles.description}>
                    {this.state.task.description}
                </div>
                <br/>
                Published: {new Date(this.state.task.publishedDate).toLocaleString()}
                <br/>
                Due date: {new Date(this.state.task.deadlineDate).toLocaleString()}
                <br/>
                {this.props.forTeacher ? "Checker id: " + this.state.task.checkerIdentifier : ""}
            </div>
        );
    }
}
