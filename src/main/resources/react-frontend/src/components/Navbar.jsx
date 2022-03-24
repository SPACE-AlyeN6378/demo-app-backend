import React, { Component } from 'react';
import Logo from './images/React-icon.svg';

class Navbar extends Component {
    
    constructor(props) {
        super(props);
    }

    render() { 
        return (
            <nav className='navbar navbar-dark bg-dark'>
                <div className='container-fluid'>
                    <a className='navbar-brand' href='#'>
                        <img src={Logo} alt="" width="30" height="30" className="d-inline-block align-text-top me-2" />
                        React Messenger
                    </a>
                    <button type='button' align='right' className='btn btn-primary'>Sign In</button>
                </div>
            
            </nav>
        );
    }
}
 
export default Navbar;