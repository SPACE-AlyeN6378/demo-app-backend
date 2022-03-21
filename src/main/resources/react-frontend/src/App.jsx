import Login from './components/Login';
import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

// import UserService from './services/UserService';
import { Link } from 'react-router-dom';
import Registration from './components/Registration';
import EmailConfirmed from './components/EmailConfirmed';


class App extends Component {

  state = {
    email: null,
    first_name: ""
  }

  
  changeEmail = (input) => {
    this.setState({email: input});
  }

  changeName = (input) => {
    this.setState({first_name: input});
  } 

  showLoginStatus() {
    
    if (this.state.email !== null && this.state.email.length !== 0) {
      return (
        <div>
          <h3 align='left'>Hello, {this.state.first_name}!</h3>
          
          <button type='button' className='btn btn-danger' onClick={this.logout}>Log Out</button>
        </div>
      );
    }
    else {
      return (
        <div>
          <h3>Hello there! You are not signed in.</h3>
          <Link type='button' className='btn btn-primary' to='/login'>Sign In</Link>
        </div>
      );
    }
  }

  // componentWillUnmount() {
  //   this.setState({email: null, first_name: ""});
  // }

  logout = () => {
    console.log(`${this.state.first_name} logged out`);
    this.setState({google_auth: false, email: null, first_name: ""});
  }


  render() { 

    return (

      <Router>
        <div className='App'>
        
          <Switch>
            <Route exact path="/">
              <div>
                <h1 align="left">Home Page</h1>
                <p align="left">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
                <br></br>
              </div>
              <div>
                {this.showLoginStatus()}
                
              </div>
            </Route>
            <Route path="/login">
              <Login app_changeEmail = {this.changeEmail} app_changeName = {this.changeName}/>
            </Route>
            <Route path="/register">
              <Registration />
            </Route>
            <Route path="/conf/:token">
              <EmailConfirmed />
            </Route>
          </Switch>
        </div>

      </Router>
    );
  }
}
 
export default App;