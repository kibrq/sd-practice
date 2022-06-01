import React from "react";
import {STUDENT_SUBMISSIONS_URL} from "./Student.js";

const axios = require("axios").default;

export default class Submission extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            submission: null
        };
    }

    getSubmission = async id => {
        let submission = null;
        let url = STUDENT_SUBMISSIONS_URL + "?id=" + id;
        await axios.get(url).then(result => {
            submission = result.data;
        });
        return submission;
    };

    componentDidMount() {
        this.getSubmission(this.props.submission.id).then(submission => {
            this.setState({
                submission: submission
            });
        }).catch(error => {
            console.log("Error occurred!");
            console.log(error);
        });
    }

    render() {
        if (this.props.submission == null || this.state.submission == null) return <span>Loading submission...</span>;

        return (
            <div>
                ID: {this.state.submission.id}
                <br/>
                Task ID: {this.state.submission.taskId}
                <br/>
                Submitted date: {new Date(this.state.submission.date).toLocaleString()}
                <br/>
                URL: <a href={this.state.submission.repositoryUrl}>{this.state.submission.repositoryUrl}</a>
                <br/>
                {
                    this.state.submission.result ?
                        "Result: " + this.state.submission.result.verdict
                        : "No result yet"
                }
            </div>
        );
    }
}
