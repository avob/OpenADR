import React from 'react';

import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationVenCard } from '../common/VtnConfigurationCard'

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
      <div className={classes.title}>
         <Typography variant="h6" id="tableTitle">
             Users
          </Typography>
      </div>
      <div className={classes.spacer} />
      <div className={classes.actions}>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Username</TableCell>
            <TableCell align="right">Roles</TableCell>
    
          </TableRow>
        </TableHead>
        <TableBody>
          {user.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.username}</TableCell>
              <TableCell align="right">{row.roles}</TableCell>
        
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


  render() {
    const {classes, user} = this.props;
    var view = [];

    return (
    <div className={ classes.root }>
      <Grid container spacing={ 8 }>
          <Grid container
                >

            <Grid item xs={ 11 }>
              <FilterPanel classes={classes}  type="VEN" />
            </Grid>
            <Grid item xs={ 1 }>
             <Button key="btn_create"
                      variant="outlined"
                      color="primary"
                      size="small"
                      className={ classes.button }
                      fullWidth={ true } 
                      onClick={ this.handleCreateUserClick }>
                <AddIcon />New
              </Button>
            </Grid>
           
          </Grid>
        </Grid> 
        <Divider style={ { marginBottom: '30px', marginTop: '20px' } } /> 
      <AccountUserTable user={user} classes={classes}/>
      
      
 
    </div>
    );
  }
}

export default AccountUser;
