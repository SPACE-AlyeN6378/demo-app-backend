import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import { withRouter } from 'react-router-dom';
import UserService from '../services/UserService';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            input_email: "",
            input_pwd: "",
            errorMessage: null
        }
    }


    handleSubmit = (event) => {
        
        event.preventDefault(); 

        // If unauthorized, catch the error
        UserService.loginCheckStatus(this.state.input_email, this.state.input_pwd)
            .then(() => {
                this.props.appChangeState(this.state.input_email);
                this.props.history.push("/");
            })
            // .catch((e) => console.log(e.response.data.message));
            .catch((e) => this.setState({errorMessage: e.response.data.message}));
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

    goToRegister = () => {
        this.props.history.push("/register");
    }


    render() { 

        return (
            <React.Fragment>
                <br></br>
                <div className="container">
                    <div className='row'>
                        <div className = "card col-md-6 offset-md-3 offset-md-3">
                            <h1 className='p-4'>Login</h1>
                            <div className='card-body'>
                                
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
                                        <input type='submit' className='btn btn-success m-3' name='Sign In' />
                                    </div>
                                    <div>
                                        <br></br><br></br>
                                        <p>New user? <button type='button' className='btn btn-secondary m-1' onClick={this.goToRegister}>Register</button></p>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </React.Fragment>
                
                   
            
                
        );  
    }
}
 
export default withRouter(Login);