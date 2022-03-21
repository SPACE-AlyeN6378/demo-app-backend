import React, { Component } from 'react';
import GoogleLogin from 'react-google-login';
import googleCredentials from '../credentials.json';
import { withRouter } from 'react-router-dom';


class GoogleButton extends Component {

    responseGoogle = (response) => {
        this.props.login_changeEmail(response.profileObj.email);
        this.props.login_changeName(response.profileObj.givenName);
        console.log(`Logged in with Google as ${response.profileObj.name}`);
        this.props.history.push("/");
    }

    errorReport = (response) => {
        console.log(response);
    }
    
    render() { 
        return ( <div>
            <GoogleLogin
            clientId={googleCredentials.web.client_id}
            buttonText='Login with Google'
            onSuccess={this.responseGoogle}
            onFailure={this.errorReport}
            cookiePolicy={'single_host_origin'}
            />        
        </div> );
    }
}
 
export default withRouter(GoogleButton);