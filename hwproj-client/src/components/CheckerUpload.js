import React from "react";
import "../css/Background.scss";
import formStyles from "../css/Form.module.scss";
import styles from "../css/Upload.module.scss";
import Sidebar from "./Sidebar.js";
import {NavLink} from "react-router-dom";
import {TEACHER_CHECKERS_URL} from "./Teacher";

const axios = require("axios").default;

export default class CheckerUpload extends React.Component {
    defaultFormState = {
        dockerfile: ""
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
                    alert("Please enter checker " + field);
                    return;
                }
            }
        }

        axios.post(TEACHER_CHECKERS_URL, {
            dockerfile: this.state.dockerfile
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
                    <textarea
                        className={formStyles.bigInput}
                        rows="9"
                        placeholder="Enter checker dockerfile"
                        value={this.state.dockerfile}
                        onChange={this.handleChange("dockerfile")}
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
