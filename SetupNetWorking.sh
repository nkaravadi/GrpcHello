export colima_host_ip=$(ifconfig bridge100 | grep "inet " | cut -d' ' -f2)
echo colima_host_ip $colima_host_ip 
export colima_vm_ip=$(colima list | grep docker | awk '{print $8}')
echo colima_vm_ip $colima_vm_ip
export colima_kind_cidr=$(docker network inspect -f '{{.IPAM.Config}}' kind | cut -d'{' -f2 | cut -d' ' -f1)
echo colima_kind_cidr $colima_kind_cidr
export colima_kind_cidr_short=$(docker network inspect -f '{{.IPAM.Config}}' kind | cut -d'{' -f2 | cut -d' ' -f1| cut -d '.' -f1-2)
echo colima_kind_cidr_short $colima_kind_cidr_short
export colima_vm_iface=$(colima ssh -- "ifconfig" | grep -B 1 $colima_vm_ip | cut -d' ' -f1)
echo colima_vm_iface $colima_vm_iface
export colima_kind_iface=$(colima ssh -- "ifconfig" | grep -B 1 $colima_kind_cidr_short | cut -d' ' -f1)
echo colima_kind_iface $colima_kind_iface
echo Setting route to send traffic to VM
echo sudo route -nv add -net $colima_kind_cidr_short $colima_vm_ip
sudo route -nv add -net $colima_kind_cidr_short $colima_vm_ip

echo
echo "Forward traffic from VM to Kind network"
echo "Run this command in colima ssh"
echo "sudo iptables -A FORWARD -s $colima_host_ip -d $colima_kind_cidr -i $colima_vm_iface -o $colima_kind_iface -p tcp -j ACCEPT"
