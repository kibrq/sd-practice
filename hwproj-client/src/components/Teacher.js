import React from "react";
import "../css/Background.scss";
import formStyle from "../css/Form.module.scss";
import Sidebar from "./Sidebar.js";

const axios = require("axios").default;
const TEACHER_TASKS = "teacher/tasks/";
const TEACHER_CHECKERS = "teacher/checkers/";
const ALL = "all";
const SERVER_TASKS_URL = process.env.REACT_APP_SERVER_API_URL + TEACHER_TASKS;
const SERVER_TASKS_ALL_URL = SERVER_TASKS_URL + ALL;
const SERVER_CHECKERS_URL = process.env.REACT_APP_SERVER_API_URL + TEACHER_CHECKERS;
const SERVER_CHECKERS_ALL_URL = SERVER_CHECKERS_URL + ALL;

export default class Teacher extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            waitForServer: true,
            tasks: null,
            checkers: null
        };
    }

    getTasks = async () => {
        console.log("ask tassk")
        let tasks = null;
        await axios.get(SERVER_TASKS_ALL_URL).then(result => {
            tasks = result.data;
        });
        return tasks;
    };

    getCheckers = async () => {
        let checkers = null;
        await axios.get(SERVER_CHECKERS_ALL_URL).then(result => {
            checkers = result.data;
        });
        return checkers;
    };

    openTask = () => {
    };

    openChecker = () => {
    };

    componentDidMount() {
        this.getTasks().then(tasks => {
            this.getCheckers().then(checkers => {
                this.setState({
                    waitForServer: false,
                    tasks: tasks,
                    checkers: checkers
                });
            });
        }).catch(error => {
            console.log("Error occurred!");
            console.log(error);
        });
    }

    render() {
        if (this.state.waitForServer) return <span>Loading...</span>;

        return (
            <div className="Teacher">
                <Sidebar/>
                <div id="bg"/>
                <div>
                    Tasks:
                    <div>
                        {this.state.tasks.length === 0 ? "No tasks yet" : this.state.tasks.map((task, index) =>
                            <li key={index}>
                                <span style={{cursor: "pointer"}} onClick={() => this.openTask(task)}>
                                    {task}
                                </span>
                            </li>
                        )}
                    </div>
                </div>
                <div>
                    Checkers:
                    <div>
                        {this.state.checkers.length === 0 ? "No checkers yet" : this.state.checkers.map((checker, index) =>
                            <li key={index}>
                                <span style={{cursor: "pointer"}} onClick={() => this.openChecker(checker)}>
                                    {checker}
                                </span>
                            </li>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}
