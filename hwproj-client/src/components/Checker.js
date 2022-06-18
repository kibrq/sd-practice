import React from "react";
import {TEACHER_CHECKERS_URL} from "./Teacher.js";
import styles from "../css/Checker.module.scss";

const axios = require("axios").default;

export default class Checker extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            checker: null
        };
    }

    getChecker = async id => {
        let checker = null;
        let url = TEACHER_CHECKERS_URL + "?id=" + id;
        await axios.get(url).then(result => {
            checker = result.data;
        });
        return checker;
    };

    componentDidMount() {
        this.getChecker(this.props.checker.id).then(checker => {
            this.setState({
                checker: checker
            });
        }).catch(error => {
            console.log("Error occurred!");
            console.log(error);
        });
    }

    render() {
        if (this.props.checker == null || this.state.checker == null) return <span>Loading checker...</span>;

        return (
            <div>
                ID: {this.state.checker.id}
                <br/>
                DOCKERFILE:
                <br/>
                <div className={styles.dockerfile}>
                    {this.state.checker.dockerfile}
                </div>
                <br/>
            </div>
        );
    }
}
