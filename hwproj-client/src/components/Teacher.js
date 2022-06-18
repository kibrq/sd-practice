import React from "react";
import "../css/Background.scss";
import styles from "../css/Teacher.module.scss";
import formStyles from "../css/Form.module.scss";
import Sidebar from "./Sidebar.js";
import Task from "./Task.js";
import {Button, Modal} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import Checker from "./Checker";

const axios = require("axios").default;
const TEACHER_TASKS = "teacher/tasks";
const TEACHER_CHECKERS = "teacher/checkers";
const LIST = "/list";
export const TEACHER_TASKS_URL = process.env.REACT_APP_SERVER_API_URL + TEACHER_TASKS;
const TEACHER_TASKS_ALL_URL = TEACHER_TASKS_URL + LIST;
export const TEACHER_CHECKERS_URL = process.env.REACT_APP_SERVER_API_URL + TEACHER_CHECKERS;
const TEACHER_CHECKERS_ALL_URL = TEACHER_CHECKERS_URL + LIST;

export default class Teacher extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            waitForServer: true,
            tasks: null,
            checkers: null,
            showTaskModal: false,
            selectedTask: null,
            showCheckerModal: false,
            selectedChecker: null
        };
    }

    getTasks = async () => {
        let tasks = null;
        await axios.get(TEACHER_TASKS_ALL_URL).then(result => {
            tasks = result.data;
        });
        return tasks;
    };

    getCheckers = async () => {
        let checkers = null;
        await axios.get(TEACHER_CHECKERS_ALL_URL).then(result => {
            checkers = result.data;
        });
        return checkers;
    };

    handleModalClose = () => this.setState({
        showTaskModal: false,
        showCheckerModal: false
    });

    openTask = id => {
        this.setState({
            showTaskModal: true,
            selectedTask: id
        });
    };

    openChecker = id => {
        this.setState({
            showCheckerModal: true,
            selectedChecker: id
        });
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
        if (this.state.waitForServer) return (
            <div>
                <div id="bg"/>
                <span>Loading...</span>
            </div>
        );

        return (
            <div className="Teacher">
                <Sidebar/>
                <div id="bg"/>
                <Modal show={this.state.showTaskModal} onHide={this.handleModalClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.state.selectedTask == null ? "" : this.state.selectedTask.name}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Task
                            task={this.state.selectedTask}
                            forTeacher={true}
                        />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleModalClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
                <Modal show={this.state.showCheckerModal} onHide={this.handleModalClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.state.selectedChecker == null ? "" : this.state.selectedChecker.id}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Checker
                            checker={this.state.selectedChecker}
                        />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleModalClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
                <div className={styles.tasks}>
                    <div className={styles.text}>
                        Tasks:
                    </div>
                    <NavLink className={formStyles.link} to="/teacher/task/upload">
                        <button className={formStyles.btn}>
                            Upload
                        </button>
                    </NavLink>
                    <div className={styles.tasksList}>
                        {this.state.tasks.length === 0 ? "No tasks yet" : this.state.tasks.map((task, index) =>
                            <li key={index}>
                                <span style={{cursor: "pointer"}} onClick={() => this.openTask(task)}>
                                    {task.name}
                                </span>
                            </li>
                        )}
                    </div>
                </div>
                <div className={styles.checkers}>
                    <div className={styles.text}>
                        Checkers:
                    </div>
                    <NavLink className={formStyles.link} to="/teacher/checker/upload">
                        <button className={formStyles.btn}>
                            Upload
                        </button>
                    </NavLink>
                    <div className={styles.checkersList}>
                        {this.state.checkers.length === 0 ? "No checkers yet" : this.state.checkers.map((checker, index) =>
                            <li key={index}>
                                <span style={{cursor: "pointer"}} onClick={() => this.openChecker(checker)}>
                                    {checker.id}
                                </span>
                            </li>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}
