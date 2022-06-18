import React from "react";
import "../css/Background.scss";
import styles from "../css/Student.module.scss";
import Sidebar from "./Sidebar.js";
import {Button, Modal} from "react-bootstrap";
import Task from "./Task";
import Submission from "./Submission";
import {NavLink} from "react-router-dom";
import formStyles from "../css/Form.module.scss";

const axios = require("axios").default;
const STUDENT_TASKS = "student/tasks/";
const STUDENT_SUBMISSIONS = "student/submissions/";
const LIST = "list";
const STUDENT_TASKS_URL = process.env.REACT_APP_SERVER_API_URL + STUDENT_TASKS;
const STUDENT_TASKS_ALL_URL = STUDENT_TASKS_URL + LIST;
export const STUDENT_SUBMISSIONS_URL = process.env.REACT_APP_SERVER_API_URL + STUDENT_SUBMISSIONS;
const STUDENT_SUBMISSIONS_ALL_URL = STUDENT_SUBMISSIONS_URL + LIST;

export default class Student extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            waitForServer: true,
            tasks: null,
            submissions: null,
            showTaskModal: false,
            selectedTask: null,
            showSubmissionModal: false,
            selectedSubmission: null
        };
    }

    getTasks = async () => {
        let tasks = null;
        await axios.get(STUDENT_TASKS_ALL_URL).then(result => {
            tasks = result.data;
        });
        return tasks;
    };

    getSubmissions = async () => {
        let submissions = null;
        await axios.get(STUDENT_SUBMISSIONS_ALL_URL).then(result => {
            submissions = result.data;
        });
        return submissions;
    };

    handleModalClose = () => this.setState({
        showTaskModal: false,
        showSubmissionModal: false
    });

    openTask = id => {
        this.setState({
            showTaskModal: true,
            selectedTask: id
        });
    };

    openSubmission = id => {
        this.setState({
            showSubmissionModal: true,
            selectedSubmission: id
        });
    };

    componentDidMount() {
        this.getTasks().then(tasks => {
            this.getSubmissions().then(submissions => {
                this.setState({
                    waitForServer: false,
                    tasks: tasks,
                    submissions: submissions
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
            <div className="Student">
                <Sidebar/>
                <div id="bg"/>
                <Modal show={this.state.showTaskModal} onHide={this.handleModalClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.state.selectedTask == null ? "" : this.state.selectedTask.name}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Task
                            task={this.state.selectedTask}
                            forTeacher={false}
                        />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleModalClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
                <Modal show={this.state.showSubmissionModal} onHide={this.handleModalClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.state.selectedSubmission == null ? "" : this.state.selectedSubmission.id}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Submission
                            submission={this.state.selectedSubmission}
                        />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleModalClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
                <div className={styles.tasks}>
                    Tasks:
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
                <div className={styles.submissions}>
                    <div className={styles.text}>
                        Submissions:
                    </div>
                    <NavLink className={formStyles.link} to="/student/submission/upload">
                        <button className={formStyles.btn}>
                            Upload
                        </button>
                    </NavLink>
                    <div className={styles.submissionsList}>
                        {this.state.submissions.length === 0 ? "No submissions yet" : this.state.submissions.map((submission, index) =>
                            <li key={index}>
                                <span style={{cursor: "pointer"}} onClick={() => this.openSubmission(submission)}>
                                    {submission.id}
                                </span>
                            </li>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}
