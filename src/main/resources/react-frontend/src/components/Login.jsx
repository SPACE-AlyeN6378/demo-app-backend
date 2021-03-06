import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import { withRouter } from 'react-router-dom';
import UserService from '../services/UserService';
import userIcon from './images/user_icon.png';
import GoogleButton from './GoogleButton';


class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            input_email: "",
            input_pwd: "",
            errorMessage: null,
            
            google_email: "",
            google_givenName: "",

            authenticated: false
        }
    }

    goToHome = () => {
        this.props.history.push("/");
    }

    goToRegister = () => {
        this.props.history.push("/register");
    }

    

    handleSubmit = (event) => {
        
        event.preventDefault(); 

        // If unauthorized, catch the error
        UserService.loginCheckStatus(this.state.input_email, this.state.input_pwd)
            .then(() => {
                this.props.app_changeEmail(this.state.input_email);

                UserService.getUser(this.state.input_email)
                    .then((res) => {
                        this.props.app_changeName(res.data.firstName);
                        console.log(`Logged in using email as ${res.data.firstName} ${res.data.lastName}`);
                    });
                
                this.props.history.push("/");
            })
            // .catch((e) => console.log(e.response.data.message));
            .catch((e) => this.setState({errorMessage: e.response.data.message}));
    }

    googleSetEmail = (input) => {
        this.props.app_changeEmail(input);
    }

    googleSetName = (input) => {
        this.props.app_changeName(input);
    }

    showErrorMsg() {
        const { errorMessage } = this.state;
        if (errorMessage !== null) {
            
            return (
                <div className="card text-white bg-danger mb-3">
                    <div className="card-body" align='left'>
                        ERROR: {errorMessage}
                    </div>
                </div>
            );
        }
    }


    render() {

        return (
            <React.Fragment>
                <br></br><br></br><br></br>
                <div className="container">
                    <div className='row'>
                        <div className = "card col-md-6 offset-md-3 offset-md-3 p-4">
                            <button type="button" className="btn-close" aria-label="Close" onClick={this.goToHome}></button>
                            <h1 className='p-4' align='center'>Login</h1>
                            <div className='card-body'>
                                <div className="text-center">
                                <img src={userIcon} alt='User Icon' width='180' height='180' />
                                </div>
                                
                                {this.showErrorMsg()}

                                
                                <form onSubmit={this.handleSubmit}>
                                    <div>
                                        <label>Email ID: </label>
                                        <input 
                                            className='form-control'
                                            placeholder='helloworld@example.com'
                                            type="text"
                                            name="email"
                                            value={this.state.input_email}
                                            onChange={(e) => this.setState({input_email: e.target.value})}
                                            required />
                                    </div>
                                    <div>
                                        <label>Password: </label>
                                        <input 
                                            className='form-control'
                                            placeholder='Password'
                                            type="password"
                                            name="pwd"
                                            value={this.state.input_pwd}
                                            onChange={(e) => this.setState({input_pwd: e.target.value})}
                                            required />
                                    </div>
                                    <div>
                                        <button type='button' className='btn btn-success m-3' onClick={this.handleSubmit}>Sign In</button>
                                    </div>
                                    <hr />
                                    
                                </form>
                                <p align='center'>New user? <button type='button' className='btn btn-secondary m-1' onClick={this.goToRegister}>Register</button></p>
                                <div align='center'>
                                    <GoogleButton login_changeEmail = {this.googleSetEmail} login_changeName = {this.googleSetName}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </React.Fragment>
                   
        );  
    }
}
 
export default withRouter(Login);