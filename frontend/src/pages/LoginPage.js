import React, {Component} from 'react';

import requireUnauth from '../hocs/requireUnauth';

class LoginPage extends Component {
    constructor(){
        super();
        this.state={}


    }
    onHandleChange= (event)=> {
        this.setState({[event.target.name]: event.target.value});
    }

    login = (user)=> {
        axios.post('http://localhost:8080/auth-service/login', {
            username: user.username,
            password: user.password
        })
            .then(function (response) {
                console.log(response);

            })
            .catch(function (error) {
                console.log(error);
            });
    }

    signUp = (user)=> {
        axios.post('http://localhost:8080/auth-service/signUp', {
            username: user.username,
            password: user.password
        })
            .then(function (response) {
                console.log(response);
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    render() {
        return (
            <div>
                <h1>Login page</h1>
                <div>
                    <input
                        name="username"
                        type="text"
                        placeholder="Username"
                        onChange={this.onHandleChange}
                        value={this.state.username}
                    />
                    <br/>
                    <input
                        name="password"
                        type="password"
                        placeholder="Password"
                        onChange={this.onHandleChange}
                        value={this.state.password}
                    />
                    <br/>

                    <button
                        style={{backgroundColor: "orange"}}
                        onClick={() =>
                            this.signUp({
                                username: this.state.username,
                                password: this.state.password
                            })
                        }
                    >
                        Sign up
                    </button>
                    <button
                        style={{backgroundColor: "#98fb98"}}

                        onClick={() =>
                            this.login({
                                username: this.state.username,
                                password: this.state.password
                            })
                        }
                    >
                        Sign in
                    </button>
                </div>
            </div>
        );
    }
}

export default requireUnauth(LoginPage);
