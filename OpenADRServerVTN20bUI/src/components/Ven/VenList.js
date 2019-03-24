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

export class VenList extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.filter = [];

  }

  handleDeleteVen = (username) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.props.deleteVen( username )

    }

  }

  handleEditVen = (username) => {
    return function ( event ) {
      event.preventDefault();
      history.push( '/ven/detail/' + username )
    }
  }

  handleCreateVENClick = () => {
    history.push( '/ven/create' )
  }

  onFilterChange = (filter) => {this.setState({filter})}

  render() {
    const {classes, ven, marketContext, group} = this.props;
    var view = [];

    for (var i in ven) {
      var v = ven[ i ];
      view.push(

        <VtnConfigurationVenCard key={ 'ven_card_' + v.username }
                                 classes={ classes }
                                 ven={ v }
                                 handleDeleteVen={ this.handleDeleteVen( v.username ) }
                                 handleEditVen={ this.handleEditVen( v.username ) } />
      );
    }
    return (
    <div className={ classes.root }>
      <Grid container spacing={ 8 }>
          <Grid container
                >

            <Grid item xs={ 11 }>
              <FilterPanel classes={classes} hasFilter={{group:true, marketContext:true, venStatus:true}} 
                group={group}
                marketContext={marketContext}
                filter={this.state.filter}
                onFilterChange={this.onFilterChange}/>
            </Grid>
            <Grid item xs={ 1 }>
             <Button key="btn_create"
                      variant="outlined"
                      color="primary"
                      size="small"
                      className={ classes.button }
                      fullWidth={ true } 
                      onClick={ this.handleCreateVENClick }>
                <AddIcon />New
              </Button>
            </Grid>
           
          </Grid>
        </Grid> 
      
      
      <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
      <GridList style={ { justifyContent: 'space-around', } }>
        { view }
      </GridList>
    </div>
    );
  }
}

export default VenList;
