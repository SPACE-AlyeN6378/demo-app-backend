import { Link } from 'react-router-dom';

const Home = (props) => {
    return ( 
        <div>
            <h1 align="left">Welcome!</h1>
            <p align="left">This is a prototype of a chat application.</p>
            <br></br>
            <div>
                <h3>You are not signed in.</h3>
                <Link type='button' className='btn btn-primary' to='/login'>Sign In</Link>
            </div>
        </div>
    );
}
 
export default Home;