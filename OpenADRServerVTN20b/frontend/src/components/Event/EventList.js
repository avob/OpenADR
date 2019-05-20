import React from 'react';



import Divider from '@material-ui/core/Divider';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'



import { history } from '../../store/configureStore';

import EventHeader from './EventHeader'



import moment from 'moment'



export class EventList extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {};
  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  handleDeleteEvent = (id) => () => {
    this.props.deleteEvent(id)
  }

  handleViewChange = (view) => () => {
    this.props.onViewChange(view);
  } 

  getTitle = () => {
    switch(this.props.view) {
      case "month":
        return moment(this.props.currentDate).format( "YYYY MMMM");

      case "week":
        let start = moment(this.props.currentDate).startOf(this.props.view);
        let end = moment(this.props.currentDate).endOf(this.props.view);
        return start.format( "MMM Do") + " - " + end.format( "MMM Do");

      case "day":
        return moment(this.props.currentDate).format( "dddd MMM DD")

      default:
        return moment(this.props.currentDate).format( "YYYY MMMM");
    }
  }


  getNext = () => {
    var d = moment(this.props.currentDate).add(1, this.props.view);
    var date = d.toDate();
    this.props.onCurrentDateChange(date);
  }

  getBack = () => {
    var d = moment(this.props.currentDate).add(-1, this.props.view);
    var date = d.toDate();
    this.props.onCurrentDateChange(date);
  }

  getToday = () => {
    var date = new Date();
    this.props.onCurrentDateChange(date);
  }

  render() {
    const {classes, marketContext, event, ven, filters, pagination, onFilterChange, onPaginationChange,
      onVenSuggestionsFetchRequested, onVenSuggestionsClearRequested, onVenSuggestionsSelect} = this.props;

    var view = [];


    for (var i in event) {
      var v = event[ i ];
      view.push(
        <VtnConfigurationEventCard key={ 'event_card_' + v.id }
                                 classes={ classes }
                                 event={ v } 

                                  handleEditEvent = {this.handleEditEvent(v.id)}
                                  handleDeleteEvent = {this.handleDeleteEvent(v.id)} />
      );
    }
    return (
      <div className={ classes.root }>
        <EventHeader classes={classes}  marketContext={marketContext} event={event}
        filters={filters} pagination={pagination} onFilterChange={onFilterChange} onPaginationChange={onPaginationChange}
        ven={ven}
                onVenSuggestionsFetchRequested={onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={onVenSuggestionsSelect}/>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <div className="rbc-calendar">
              <div className="rbc-toolbar">
                <span className="rbc-btn-group">
                  <button type="button"
                    onClick={this.getToday}>Today</button>
                  <button type="button"
                    onClick={this.getBack}>Back</button>
                  <button type="button"
                    onClick={this.getNext}>Next</button>
                </span>  
               
                <span className="rbc-toolbar-label ">
                  
                  <span style={{paddingTop:10}}>{this.getTitle()}</span>
                  <span className="rbc-btn-group" style={{float:"right", visibility:"hidden"}}>
                    <button type="button">Color Status</button>
                    <button type="button">Color Market</button>
                  </span>  
                </span>  
                <span className="rbc-btn-group">
                  <button type="button" className={(this.state.view === "month") ? "rbc-active" : ""}
                    onClick={this.handleViewChange("month")}>Month</button>
                  <button type="button" className={(this.state.view === "week") ? "rbc-active" : ""}
                    onClick={this.handleViewChange("week")}>Week</button>
                  <button type="button" className={(this.state.view === "day") ? "rbc-active" : ""}
                    onClick={this.handleViewChange("day")}>Day</button>
                  <button type="button" style={{visibility:"hidden"}}>Agenda</button>
                </span>

                 
              </div>
            </div>
        <GridList style={ { justifyContent: 'left', } }>
          { view }
        </GridList>
      </div>
    );
  }
}

export default EventList;
