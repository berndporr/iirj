## Copyright (C) 2016 Bernd Porr
## 
## This program is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <http://www.gnu.org/licenses/>.

## -*- texinfo -*- 
## @deftypefn {Function File} {@var{retval} =} filtertest (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Bernd Porr <bp1@bp1-Precision-WorkStation-T5400>
## Created: 2016-10-08

load "lp.txt"
fy = abs(fft(lp));
fx = linspace(0,250,length(lp));
subplot(2,2,1);
plot(fx,fy);
axis([ 0 125 0 1]);
title("Lowpass");
xlabel("f/Hz");
#
load "hp.txt"
fy = abs(fft(hp));
fx = linspace(0,250,length(lp));
subplot(2,2,2);
plot(fx,fy);
axis([ 0 125 0 1]);
title("Highpass");
xlabel("f/Hz");
#
load "bp.txt"
fy = abs(fft(bp));
fx = linspace(0,250,length(lp));
subplot(2,2,3);
plot(fx,fy);
axis([ 0 125 0 1]);
title("Bandpass");
xlabel("f/Hz");
#
load "bs.txt"
fy = abs(fft(bs));
fx = linspace(0,250,length(lp));
subplot(2,2,4);
plot(fx,fy);
axis([ 0 125 0 1]);
xlabel("f/Hz");
title("Bandstop");
print 'filtertest.png'
