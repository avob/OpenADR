apt_update 'update'

# Configure the MySQL client.
mysql_client 'default' do
  action :create
end

# Configure the MySQL service.
mysql_service 'default' do
  port '3306'
  bind_address '0.0.0.0'
  version node['mysql']['version']
  action [:create, :start]
end