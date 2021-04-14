import React from 'react';

import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';





import { history } from '../../store/configureStore';
import Grid from '@material-ui/core/Grid';


import FilterPanel from '../common/FilterPanel' 

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import Toolbar from '@material-ui/core/Toolbar';

var AccountUserTable = (props) => {
  const {classes, user} = props

  return (
    <Paper className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Users
          </Typography>
      </div>

      <Button key="btn_create"
                      variant="outlined"
                      color="primary"
                      size="small"
                      className={ classes.button }
                      onClick={ props.handleCreateUserClick }>
                New
              </Button>



    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Common Name</TableCell>
            <TableCell align="right">Username</TableCell>
            <TableCell align="right">Authentication Method</TableCell>
            <TableCell align="right">Roles</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {user.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.commonName}</TableCell>
              <TableCell align="right">{row.username}</TableCell>
              <TableCell align="right">{row.authenticationType}</TableCell>
              <TableCell align="right">{row.roles.map(role => {
                return (<span key={role}>
                    {role} <br/>
                    </span>
                  );
              })}</TableCell>
              <TableCell scope="row" align="right">
                  <Button size="small" color="secondary" onClick={props.handleDeleteUser(row.username)}> REMOVE </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}



export class AccountUser extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  handleCreateUserClick = () => {
    history.push( '/account/user/create' )
  }

  handleDeleteUser = (username) => () => {
    this.props.deleteUser(username);
  }

  render() {
    const {classes, user} = this.props;

    return (
    <div className={ classes.root }>
      <AccountUserTable user={user} classes={classes} handleDeleteUser={this.handleDeleteUser} handleCreateUserClick={this.handleCreateUserClick}/>
      
      
 
    </div>
    );
  }
}

export default AccountUser;
