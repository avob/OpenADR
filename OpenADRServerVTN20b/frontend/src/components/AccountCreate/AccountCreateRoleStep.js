import React from 'react';


import Grid from '@material-ui/core/Grid';

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';


import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';

const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,

  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
  marginTop: "-8px"
}

var acceptableRoles = [{
  name:"Admin",
  value: "ROLE_ADMIN"
},{
  name:"Device Manager",
  value: "ROLE_DEVICE_MANAGER"
},{
  name:"Demand Response Program",
  value: "ROLE_DRPROGRAM"
}]

var AccountCreateRoleTable = (props) => {
  const {classes} = props;
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Role</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.roles.map( (row, index) => (
            <TableRow key={index}>
              <TableCell scope="row" align="right">{row}</TableCell>

              <TableCell scope="row" align="right">
                  <Button size="small" color="secondary" onClick={props.removeRole(index)}> REMOVE </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}

export class AccountCreateRoleStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {
      selectedRole: ""
    };
  }

  clearRole = () => {
    this.setState({selectedRole: ""});
  }

  addRole = () => {
    var roles = this.props.roles;
    roles.push(this.state.selectedRole);
    this.setState({ selectedRole: ""})
    this.props.onChange(roles);
  }

  removeRole = (index) => () => {
    var roles = this.props.roles;
    roles.splice(index,1);
    this.props.onChange(roles);
  }

  handleSelectedRoleChange = (e) => {
    this.setState({selectedRole: e.target.value});
  }

  render() {
    const {classes} = this.props;

    return (
      <Grid container
          spacing={ 8 }
          justify="center">
      
      <Grid container spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 6 }>
               <FormControl className={ classes.formControl }>
                <FormLabel style={ labelStyle } component="label">
                  Select Role
                </FormLabel>
                <Select required value={ this.state.selectedRole} 
                              style={ { marginTop: 0, width:"100%" } }

                               onChange={this.handleSelectedRoleChange}
                              inputProps={ { name: 'signal_name_select'
                                , id: 'signal_name_select'} }
                              >
                     

                   {acceptableRoles.map( (row, index) => (
                      <MenuItem key={"menu_item_acceptable_role_"+row.value} value={row.value}>
                     {row.name}
                     </MenuItem> 
                    ))}
                </Select>
              </FormControl>
          </Grid>
          { (this.state.selectedRole !== "") ? <Grid item xs={ 1 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            fullWidth={true}
                            className={ classes.button }
                            style={ { marginTop: 13 } }
                            onClick={ this.addRole }>
                      <AddIcon />Create
                    </Button>
          </Grid> : null}

          { (this.state.selectedRole !== "") ? <Grid item xs={ 1 }>
            <Button key="btn_cancel"
                            variant="outlined"
                            color="secondary"
                            size="small"
                            fullWidth={true}
                            className={ classes.button }
                            style={ { marginTop: 13 } }
                            onClick={ this.clearRole }>
                      <AddIcon />Cancel
                    </Button>
          </Grid> : null}
          <Grid item xs={ 2 } />
      </Grid>
      <Grid container spacing={ 24 }
         style={ { marginTop: 20, marginBottom:10 } }>
         <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          
          <AccountCreateRoleTable roles={this.props.roles} classes={classes} removeRole={this.removeRole}/>
        </Grid> 
        <Grid item xs={ 2 } />
      </Grid>
      
    </Grid>
    );
  }
}

export default AccountCreateRoleStep;
