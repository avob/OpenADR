
import config from "configuration";

export const notify = function(title, message, level){
	return {title, message, level};
}

export const notifySuccess = function(message){
	return {title:"Success!", message:message, level:"success"};
}

export const notifyError = function(message){
	return {title:"Error!", message:message, level:"error"};
}

export const displayNotification = function(notificationSystem, props){
	if(props.notification){
      notificationSystem.addNotification(props.notification);
    }
}


export default function notificationSystemMiddleware() {
  return ({ dispatch, getState }) => next => action => {
    
    if (typeof action === 'function') {
      return action(dispatch, getState);
    }

    if(action.notification && config.notificationSystem != null){
      config.notificationSystem.addNotification(action.notification);
    }

    return next(action);
    
  }
}
